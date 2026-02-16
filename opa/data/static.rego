package static

role_permissions := {
  "ADMIN": ["login", "transfer", "approve", "view"],
  "AUDITOR": ["login", "view"],
  "STANDARD": ["login", "transfer", "view"],
  "PREFERRED": ["login", "transfer", "view"],
  "PREMIUM": ["login", "transfer", "view"]
}

transfer_limits := {
  "STANDARD": 100000,
  "PREFERRED": 200000,
  "PREMIUM": 500000,
  "ADMIN": 1000000000,
}

fee_multipliers := {
  "STANDARD": 1.0,
  "PREFERRED": 0.5,
  "PREMIUM": 0.0,
  "ADMIN": 1.0,
  "AUDITOR": 1.0,
}

errors := {
  "TRANSFER_LIMIT": {
    "code": "TRANSFER_LIMIT_EXCEEDED",
    "http_status": 403,
    "message": "Transfer amount exceeds allowed limit"
  }
}

