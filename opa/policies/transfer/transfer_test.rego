package transfer.decision

test_standard_within_limit if {
  res := result with input as {
    "role": "STANDARD",
    "amount": 5000
  }

  res.allow == true
  res.fee_multiplier == 1.0
  res.approval_required == false
  not res.error
}

test_standard_over_limit if {
  res := result with input as {
    "role": "STANDARD",
    "amount": 200000
  }

  res.allow == false
  res.error.code == "TRANSFER_LIMIT_EXCEEDED"
}

test_preferred_fee_discount if {
  res := result with input as {
    "role": "PREFERRED",
    "amount": 5000
  }

  res.allow == true
  res.fee_multiplier == 0.5
  res.approval_required == false
}

test_premium_fee_waived if {
  res := result with input as {
    "role": "PREMIUM",
    "amount": 5000
  }

  res.allow == true
  res.fee_multiplier == 0.0
  res.approval_required == false
}

test_preferred_requires_approval_over_100k if {
  res := result with input as {
    "role": "PREFERRED",
    "amount": 150000
  }

  res.allow == true
  res.approval_required == true
}
