package models

import (
	"time"

	"gorm.io/gorm"
)

type OperationType struct {
	ID          uint           `json:"operation_type_id" gorm:"primaryKey" example:"1"`
	Description string         `json:"description" gorm:"not null" example:"Normal Purchase"`
	CreatedAt   time.Time      `json:"created_at"`
	UpdatedAt   time.Time      `json:"updated_at"`
	DeletedAt   gorm.DeletedAt `json:"-" gorm:"index"`
}

func (OperationType) TableName() string {
	return "operation_types"
}
