package auth.decision

test_standard_can_transfer if {
  res := result with input as {
    "role": "STANDARD",
    "action": "transfer"
  }

  res.allow == true
}

test_auditor_cannot_transfer if {
  res := result with input as {
    "role": "AUDITOR",
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