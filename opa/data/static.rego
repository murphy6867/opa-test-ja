package static

role_permissions := {
  "approval": ["login", "transfer", "approve", "view"],
  "user": ["login", "transfer", "view"],
  "viewer": ["login", "view"]
}

transfer_limits := {
  "user": 100000,
}

errors := {
  "TRANSFER_LIMIT": {
    "code": "TRANSFER_LIMIT_EXCEEDED",
    "http_status": 403,
    "message": "Transfer amount exceeds allowed limit"
  }
}

