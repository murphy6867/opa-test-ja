package static

role_permissions := {
  "admin": ["login", "transfer", "approve"],
  "employee": ["login", "transfer"],
  "viewer": ["login"]
}

transfer_limits := {
  "employee": 10000,
  "admin": 100000
}

errors := {
  "TRANSFER_LIMIT": {
    "code": "TRANSFER_LIMIT_EXCEEDED",
    "http_status": 403,
    "message": "Transfer amount exceeds allowed limit"
  }
}