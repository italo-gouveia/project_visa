package services

import (
	"errors"
	"strings"
	"time"
	"visa-payment-service/internal/dto"
	"visa-payment-service/internal/models"
	"visa-payment-service/internal/repository"
)

type TransactionService struct {
	transactionRepo   *repository.TransactionRepository
	accountRepo       *repository.AccountRepository
	operationTypeRepo *repository.OperationTypeRepository
}

func NewTransactionService(
	transactionRepo *repository.TransactionRepository,
	accountRepo *repository.AccountRepository,
	operationTypeRepo *repository.OperationTypeRepository,
) *TransactionService {
	return &TransactionService{
		transactionRepo:   transactionRepo,
		accountRepo:       accountRepo,
		operationTypeRepo: operationTypeRepo,
	}
}

func (s *TransactionService) CreateTransaction(req *dto.TransactionRequest) (*models.Transaction, error) {
	// Validate account exists
	account, err := s.accountRepo.GetByID(req.AccountID)
	if err != nil {
		return nil, errors.New("account not found")
	}

	// Validate operation type exists
	operationType, err := s.operationTypeRepo.GetByID(req.OperationTypeID)
	if err != nil {
		return nil, errors.New("operation type not found")
	}

	// Apply business rules for amount sign based on operation type
	amount := req.Amount
	operationDescription := strings.ToLower(operationType.Description)

	// Purchase and withdrawal transactions should be negative
	if strings.Contains(operationDescription, "purchase") || strings.Contains(operationDescription, "withdrawal") {
		amount = -amount
	}
	// Credit voucher transactions should be positive (already positive from validation)

	transaction := &models.Transaction{
		AccountID:       req.AccountID,
		OperationTypeID: req.OperationTypeID,
		Amount:          amount,
		EventDate:       time.Now(),
		Account:         *account,
		OperationType:   *operationType,
	}

	err = s.transactionRepo.Create(transaction)
	if err != nil {
		return nil, err
	}

	// Reload with relations
	transaction, err = s.transactionRepo.GetByID(transaction.ID)
	if err != nil {
		return nil, err
	}

	return transaction, nil
}
