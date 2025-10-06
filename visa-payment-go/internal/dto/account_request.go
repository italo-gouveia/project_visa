package dto

type AccountRequest struct {
	DocumentNumber string `json:"document_number" validate:"required,len=11,numeric" example:"12345678900"`
}

type AccountResponse struct {
	AccountID      uint   `json:"account_id" example:"1"`
	DocumentNumber string `json:"document_number" example:"12345678900"`
}
