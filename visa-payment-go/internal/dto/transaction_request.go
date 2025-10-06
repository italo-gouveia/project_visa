package dto

type TransactionRequest struct {
	AccountID       uint    `json:"account_id" validate:"required" example:"1"`
	OperationTypeID uint    `json:"operation_type_id" validate:"required" example:"1"`
	Amount          float64 `json:"amount" validate:"required,gt=0" example:"50.00"`
}
