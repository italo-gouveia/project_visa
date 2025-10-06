package models

import (
	"time"

	"gorm.io/gorm"
)

type Transaction struct {
	ID              uint           `json:"transaction_id" gorm:"primaryKey" example:"1"`
	AccountID       uint           `json:"account_id" gorm:"not null" validate:"required" example:"1"`
	OperationTypeID uint           `json:"operation_type_id" gorm:"not null" validate:"required" example:"1"`
	Amount          float64        `json:"amount" gorm:"not null" validate:"required,gt=0" example:"50.00"`
	EventDate       time.Time      `json:"event_date" gorm:"not null"`
	CreatedAt       time.Time      `json:"created_at"`
	UpdatedAt       time.Time      `json:"updated_at"`
	DeletedAt       gorm.DeletedAt `json:"-" gorm:"index"`

	// Relations
	Account       Account       `json:"account" gorm:"foreignKey:AccountID"`
	OperationType OperationType `json:"operation_type" gorm:"foreignKey:OperationTypeID"`
}

func (Transaction) TableName() string {
	return "transactions"
}
