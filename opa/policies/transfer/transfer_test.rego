package transfer.decision

test_employee_within_limit if {
  res := result with input as {
    "role": "employee",
    "amount": 5000
  }

  res.allow == true
  not res.error
}

test_employee_over_limit if {
  res := result with input as {
    "role": "employee",
    "amount": 20000
  }

  res.allow == false
  res.error.code == "TRANSFER_LIMIT_EXCEEDED"
}
