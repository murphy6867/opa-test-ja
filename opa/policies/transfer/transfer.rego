package transfer.decision

default result := {
  "allow": false,
  "error": {
    "code": "TRANSFER_DENIED",
    "message": "Transfer not allowed",
    "http_status": 403
  }
}

result := {
  "allow": true,
  "fee_multiplier": data.static.fee_multipliers[input.role],
  "approval_required": input.amount > 100000
} if {
  data.static.transfer_limits[input.role]
  data.static.fee_multipliers[input.role]
  input.amount <= data.static.transfer_limits[input.role]
}

result := {
  "allow": false,
  "error": data.static.errors["TRANSFER_LIMIT"]
} if {
  data.static.transfer_limits[input.role]
  input.amount > data.static.transfer_limits[input.role]
}