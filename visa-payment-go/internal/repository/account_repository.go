package repository

import (
	"visa-payment-service/internal/models"

	"gorm.io/gorm"
)

type AccountRepository struct {
	db *gorm.DB
}

func NewAccountRepository(db *gorm.DB) *AccountRepository {
	return &AccountRepository{db: db}
}

func (r *AccountRepository) Create(account *models.Account) error {
	return r.db.Create(account).Error
}

func (r *AccountRepository) GetByID(id uint) (*models.Account, error) {
	var account models.Account
	err := r.db.First(&account, id).Error
	if err != nil {
		return nil, err
	}
	return &account, nil
}

func (r *AccountRepository) GetByDocumentNumber(documentNumber string) (*models.Account, error) {
	var account models.Account
	err := r.db.Where("document_number = ?", documentNumber).First(&account).Error
	if err != nil {
		return nil, err
	}
	return &account, nil
}

func (r *AccountRepository) ExistsByDocumentNumber(documentNumber string) (bool, error) {
	var count int64
	err := r.db.Model(&models.Account{}).Where("document_number = ?", documentNumber).Count(&count).Error
	return count > 0, err
}
