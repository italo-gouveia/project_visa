package repository

import (
	"visa-payment-service/internal/models"

	"gorm.io/gorm"
)

type TransactionRepository struct {
	db *gorm.DB
}

func NewTransactionRepository(db *gorm.DB) *TransactionRepository {
	return &TransactionRepository{db: db}
}

func (r *TransactionRepository) Create(transaction *models.Transaction) error {
	return r.db.Create(transaction).Error
}

func (r *TransactionRepository) GetByID(id uint) (*models.Transaction, error) {
	var transaction models.Transaction
	err := r.db.Preload("Account").Preload("OperationType").First(&transaction, id).Error
	if err != nil {
		return nil, err
	}
	return &transaction, nil
}

func (r *TransactionRepository) GetByAccountID(accountID uint) ([]models.Transaction, error) {
	var transactions []models.Transaction
	err := r.db.Preload("Account").Preload("OperationType").Where("account_id = ?", accountID).Find(&transactions).Error
	return transactions, err
}
