import json
import time
import uuid
from dataclasses import dataclass
from decimal import Decimal
from typing import Optional
from urllib.request import Request, urlopen


def http_json(method: str, url: str, payload: Optional[dict] = None) -> dict:
    data = None
    headers = {"Accept": "application/json"}
    if payload is not None:
        data = json.dumps(payload).encode("utf-8")
        headers["Content-Type"] = "application/json"

    req = Request(url, data=data, headers=headers, method=method)
    try:
        with urlopen(req, timeout=20) as resp:
            body = resp.read().decode("utf-8")
            if not body:
                return {}
            return json.loads(body)
    except Exception as e:
        raise RuntimeError(f"HTTP {method} {url} failed: {e}") from e


@dataclass
class UserInfo:
    user_id: str
    account_number: str


def create_user(auth_base: str, username: str, role: str, balance: Decimal, bank_code: str = "SCB") -> UserInfo:
    res = http_json(
        "POST",
        f"{auth_base}/v1/auth/sign-up",
        {
            "username": username,
            "password": "pass",
            "accountName": username,
            "role": role,
            "bankCode": bank_code,
            "balance": float(balance),
        },
    )

    data = (res or {}).get("data") or {}
    user_id = data.get("id")
    account_number = data.get("accountNumber")
    if not user_id or not account_number:
        raise RuntimeError(f"Unexpected sign-up response: {res}")
    return UserInfo(user_id=user_id, account_number=account_number)


def review_transfer(
    transfer_base: str,
    user_id: str,
    from_acc: str,
    to_acc: str,
    amount: Decimal,
    bank_code: str = "SCB",
    note: str = "test",
) -> dict:
    return http_json(
        "POST",
        f"{transfer_base}/v1/transfers/review",
        {
            "userId": user_id,
            "fromAccount": from_acc,
            "toAccount": to_acc,
            "bankCode": bank_code,
            "amount": float(amount),
            "note": note,
        },
    )


def submit_transfer(transfer_base: str, review_token: str, otp_code: str = "123456") -> dict:
    return http_json(
        "POST",
        f"{transfer_base}/v1/transfers/submit",
        {"reviewToken": review_token, "otpCode": otp_code},
    )


def main() -> None:
    auth_base = "http://localhost:8080"
    transfer_base = "http://localhost:8081"

    suffix = str(int(time.time())) + "-" + uuid.uuid4().hex[:6]
    from_user = create_user(auth_base, f"from-{suffix}", "PREFERRED", Decimal("300000"))
    to_user = create_user(auth_base, f"to-{suffix}", "STANDARD", Decimal("0"))

    print(f"FROM_ID={from_user.user_id}")
    print(f"FROM_ACC={from_user.account_number}")
    print(f"TO_ACC={to_user.account_number}")

    # small amount: expect PREFERRED multiplier 0.5, default feeRate 0.01 => fee = amount*0.01*0.5
    amount_small = Decimal("1000")
    expected_fee_small = (amount_small * Decimal("0.01") * Decimal("0.5")).quantize(Decimal("0.01"))

    print("\n--- review small amount (1000) ---")
    review1 = review_transfer(
        transfer_base,
        from_user.user_id,
        from_user.account_number,
        to_user.account_number,
        amount_small,
        note="small",
    )
    data1 = (review1 or {}).get("data") or {}
    fee1 = Decimal(str(data1.get("fee")))
    total1 = Decimal(str(data1.get("totalAmount")))
    approval1 = data1.get("approvalRequired")
    token1 = data1.get("reviewToken")
    print(f"fee={fee1} expected={expected_fee_small}")
    print(f"totalAmount={total1}")
    print(f"approvalRequired={approval1}")

    if fee1 != expected_fee_small:
        raise RuntimeError(f"fee mismatch: got {fee1}, expected {expected_fee_small}")
    if not token1:
        raise RuntimeError(f"missing reviewToken: {review1}")

    print("\n--- submit small amount ---")
    submit1 = submit_transfer(transfer_base, token1)
    print(json.dumps(submit1, indent=2))

    # large amount: should trigger approval_required (amount > 100000)
    amount_large = Decimal("150000")
    expected_fee_large = (amount_large * Decimal("0.01") * Decimal("0.5")).quantize(Decimal("0.01"))

    print("\n--- review large amount (150000) ---")
    review2 = review_transfer(
        transfer_base,
        from_user.user_id,
        from_user.account_number,
        to_user.account_number,
        amount_large,
        note="large",
    )
    data2 = (review2 or {}).get("data") or {}
    fee2 = Decimal(str(data2.get("fee")))
    total2 = Decimal(str(data2.get("totalAmount")))
    approval2 = data2.get("approvalRequired")
    token2 = data2.get("reviewToken")

    print(f"fee={fee2} expected={expected_fee_large}")
    print(f"totalAmount={total2}")
    print(f"approvalRequired={approval2} (expected True)")

    if fee2 != expected_fee_large:
        raise RuntimeError(f"fee mismatch: got {fee2}, expected {expected_fee_large}")
    if approval2 is not True:
        raise RuntimeError(f"approvalRequired mismatch: got {approval2}, expected True")
    if not token2:
        raise RuntimeError(f"missing reviewToken: {review2}")

    print("\n--- submit large amount ---")
    submit2 = submit_transfer(transfer_base, token2)
    print(json.dumps(submit2, indent=2))


if __name__ == "__main__":
    main()
