package models

import (
	"time"

	"gorm.io/gorm"
)

type Account struct {
	ID             uint           `json:"account_id" gorm:"primaryKey" example:"1"`
	DocumentNumber string         `json:"document_number" gorm:"uniqueIndex;not null" validate:"required,len=11,numeric" example:"12345678900"`
	CreatedAt      time.Time      `json:"created_at"`
	UpdatedAt      time.Time      `json:"updated_at"`
	DeletedAt      gorm.DeletedAt `json:"-" gorm:"index"`
}

func (Account) TableName() string {
	return "accounts"
}
