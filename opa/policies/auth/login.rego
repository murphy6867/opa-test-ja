package auth.decision

default result := {
  "allow": false,
  "error": {
    "code": "RBAC_DENIED",
    "message": "Access denied",
    "http_status": 403
  }
}

result := {
  "allow": true
} if {
  data.static.role_permissions[input.role]
  input.action in data.static.role_permissions[input.role]
}
