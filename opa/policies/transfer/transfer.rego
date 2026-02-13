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
  "allow": true
} if {
  data.static.transfer_limits[input.role]
  input.amount <= data.static.transfer_limits[input.role]
}

result := {
  "allow": false,
  "error": data.static.errors["TRANSFER_LIMIT"]
} if {
  data.static.transfer_limits[input.role]
  input.amount > data.static.transfer_limits[input.role]
}