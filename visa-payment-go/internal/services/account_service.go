package services

import (
	"errors"
	"visa-payment-service/internal/dto"
	"visa-payment-service/internal/models"
	"visa-payment-service/internal/repository"
)

type AccountService struct {
	accountRepo *repository.AccountRepository
}

func NewAccountService(accountRepo *repository.AccountRepository) *AccountService {
	return &AccountService{
		accountRepo: accountRepo,
	}
}

func (s *AccountService) CreateAccount(req *dto.AccountRequest) (*dto.AccountResponse, error) {
	// Check if account with this document number already exists
	exists, err := s.accountRepo.ExistsByDocumentNumber(req.DocumentNumber)
	if err != nil {
		return nil, err
	}
	if exists {
		return nil, errors.New("account with this document number already exists")
	}

	account := &models.Account{
		DocumentNumber: req.DocumentNumber,
	}

	err = s.accountRepo.Create(account)
	if err != nil {
		return nil, err
	}

	return &dto.AccountResponse{
		AccountID:      account.ID,
		DocumentNumber: account.DocumentNumber,
	}, nil
}

func (s *AccountService) GetAccount(id uint) (*dto.AccountResponse, error) {
	account, err := s.accountRepo.GetByID(id)
	if err != nil {
		return nil, err
	}

	return &dto.AccountResponse{
		AccountID:      account.ID,
		DocumentNumber: account.DocumentNumber,
	}, nil
}
