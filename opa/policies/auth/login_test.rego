package auth.decision

test_employee_can_transfer if {
  res := result with input as {
    "role": "employee",
    "action": "transfer"
  }

  res.allow == true
}

test_viewer_cannot_transfer if {
  res := result with input as {
    "role": "viewer",
    "action": "transfer"
  }

  res.allow == false
}

# test_admin_can_approve if {
#   result with input as {
#     "role": "admin",
#     "action": "approve"
#   }

#   result.allow == true
# }