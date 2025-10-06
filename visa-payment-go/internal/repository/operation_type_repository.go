package repository

import (
	"visa-payment-service/internal/models"

	"gorm.io/gorm"
)

type OperationTypeRepository struct {
	db *gorm.DB
}

func NewOperationTypeRepository(db *gorm.DB) *OperationTypeRepository {
	return &OperationTypeRepository{db: db}
}

func (r *OperationTypeRepository) GetByID(id uint) (*models.OperationType, error) {
	var operationType models.OperationType
	err := r.db.First(&operationType, id).Error
	if err != nil {
		return nil, err
	}
	return &operationType, nil
}

func (r *OperationTypeRepository) CreateIfNotExists(operationType *models.OperationType) error {
	var existing models.OperationType
	err := r.db.Where("description = ?", operationType.Description).First(&existing).Error
	if err == gorm.ErrRecordNotFound {
		return r.db.Create(operationType).Error
	}
	return err
}
