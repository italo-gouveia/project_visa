package handlers

import (
	"net/http"
	"strconv"
	"visa-payment-service/internal/dto"
	"visa-payment-service/internal/services"

	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
)

type AccountHandler struct {
	accountService *services.AccountService
	validator      *validator.Validate
}

func NewAccountHandler(accountService *services.AccountService) *AccountHandler {
	return &AccountHandler{
		accountService: accountService,
		validator:      validator.New(),
	}
}

// CreateAccount godoc
// @Summary Create a new account
// @Description Creates a new account with the provided document number
// @Tags accounts
// @Accept json
// @Produce json
// @Param account body dto.AccountRequest true "Account creation request"
// @Success 201 {object} dto.AccountResponse
// @Failure 400 {object} map[string]string
// @Failure 500 {object} map[string]string
// @Router /accounts [post]
func (h *AccountHandler) CreateAccount(c *gin.Context) {
	var req dto.AccountRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request body"})
		return
	}

	if err := h.validator.Struct(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	response, err := h.accountService.CreateAccount(&req)
	if err != nil {
		if err.Error() == "account with this document number already exists" {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Internal server error"})
		return
	}

	c.JSON(http.StatusCreated, response)
}

// GetAccount godoc
// @Summary Get account by ID
// @Description Retrieves account information by account ID
// @Tags accounts
// @Accept json
// @Produce json
// @Param id path int true "Account ID"
// @Success 200 {object} dto.AccountResponse
// @Failure 400 {object} map[string]string
// @Failure 404 {object} map[string]string
// @Failure 500 {object} map[string]string
// @Router /accounts/{id} [get]
func (h *AccountHandler) GetAccount(c *gin.Context) {
	idStr := c.Param("id")
	id, err := strconv.ParseUint(idStr, 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid account ID"})
		return
	}

	response, err := h.accountService.GetAccount(uint(id))
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "Account not found"})
		return
	}

	c.JSON(http.StatusOK, response)
}
