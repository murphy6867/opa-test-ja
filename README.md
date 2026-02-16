How to re-run the E2E quickly

```
docker compose up -d --build
```

```
python3 tools/e2e_transfer_flow.py
```

---

## Client API Reference (Swagger-like)

> Base URLs (default docker-compose ports)

- Auth Service: `http://localhost:8080`
- Transfer Service: `http://localhost:8081`
- Approval Service: `http://localhost:8082`

Notes:

- Most endpoints return an `ApiResponse<T>` wrapper.
- `transfer-service` responses include `traceId` and `errors` fields.

### Auth Service (`:8080`)

#### Health

- `GET /v1/auth/health`

Response (plain text):

```text
OK
```

#### Sign up

- `POST /v1/auth/sign-up`

Request body:

```json
{
	"username": "alice",
	"password": "pass",
	"accountName": "Alice",
	"role": "PREFERRED",
	"bankCode": "SCB",
	"balance": 300000
}
```

Response body (example):

```json
{
	"success": true,
	"message": "User created",
	"data": {
		"id": "<uuid>",
		"username": "alice",
		"balance": 300000,
		"accountName": "Alice",
		"accountNumber": "<10-digit>",
		"role": "PREFERRED",
		"bankCode": "SCB",
		"bankName": "Siam Commercial Bank"
	},
	"timestamp": "2026-02-16T08:18:08.000Z"
}
```

#### Sign in

- `POST /v1/auth/sign-in`

Request body:

```json
{
	"username": "alice",
	"password": "pass"
}
```

#### Get role by userId

- `GET /v1/auth/getRoleByUserId/{userId}`

Example:

```bash
curl http://localhost:8080/v1/auth/getRoleByUserId/<uuid>
```

### Accounts (`:8080`)

#### Batch accounts

- `POST /v1/accounts/batch-accounts`

Request body:

```json
{
	"accountNumbers": ["1234567890", "0987654321"]
}
```

#### Debit account

- `POST /v1/accounts/debit`

Request body:

```json
{
	"accountNumber": "1234567890",
	"amount": 1005.00,
	"reference": "<idempotency-key>"
}
```

Response body (example):

```json
{
	"success": true,
	"message": "Debited",
	"data": {
		"accountNumber": "1234567890",
		"debitedAmount": 1005.00,
		"newBalance": 298995.00
	},
	"timestamp": "2026-02-16T08:18:08.000Z"
}
```

### Transfer Service (`:8081`)

#### Health

- `GET /v1/transfers/check`

Response (plain text):

```text
OK
```

#### Review transfer (OPA check + fee calc + reviewToken)

- `POST /v1/transfers/review`

Request body:

```json
{
	"userId": "<uuid>",
	"fromAccount": "1234567890",
	"toAccount": "0987654321",
	"bankCode": "SCB",
	"amount": 1000.00,
	"note": "test"
}
```

Response body (example):

```json
{
	"success": true,
	"message": "Transfer review successful",
	"errors": null,
	"data": {
		"fromAccountName": "...",
		"toAccountName": "...",
		"toBankCode": "SCB",
		"amount": 1000.00,
		"fee": 5.00,
		"totalAmount": 1005.00,
		"exchangeRate": null,
		"approvalRequired": false,
		"reviewToken": "<jwt>"
	},
	"traceId": "<uuid>",
	"timestamp": "2026-02-16T08:18:08.000Z"
}
```

#### Submit transfer (consume reviewToken + debit + persist transfer/event)

- `POST /v1/transfers/submit`

Request body:

```json
{
	"reviewToken": "<jwt>",
	"otpCode": "123456"
}
```

Response body (example):

```json
{
	"success": true,
	"message": "Transfer submitted successfully",
	"errors": null,
	"data": {
		"transactionId": "<uuid>",
		"status": "SUBMITTED",
		"submittedAt": "2026-02-16T08:18:08.891Z"
	},
	"traceId": "<uuid>",
	"timestamp": "2026-02-16T08:18:08.915Z"
}
```

`status` comes from `TransferEventType`:

- `SUBMITTED` (no approval needed)
- `WAITING_APPROVAL` (approval needed)

### Approval Service (`:8082`)

#### Health

- `GET /v1/approval/health`

Response intended (plain text):

```text
OK
```