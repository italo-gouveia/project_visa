package main

import (
	"log"
	"visa-payment-service/internal/config"
	"visa-payment-service/internal/database"
	"visa-payment-service/internal/handlers"
	"visa-payment-service/internal/models"
	"visa-payment-service/internal/repository"
	"visa-payment-service/internal/services"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
)

// @title Visa Payment Service API
// @version 1.0
// @description A payment processing service for Visa
// @termsOfService http://swagger.io/terms/

// @contact.name API Support
// @contact.url http://www.swagger.io/support
// @contact.email support@swagger.io

// @license.name MIT
// @license.url https://opensource.org/licenses/MIT

// @host localhost:8080
// @BasePath /api/v1
func main() {
	// Load configuration
	cfg := config.Load()

	// Initialize database
	db, err := database.Initialize(cfg.DatabaseURL)
	if err != nil {
		log.Fatal("Failed to initialize database:", err)
	}

	// Auto-migrate models
	err = db.AutoMigrate(&models.Account{}, &models.OperationType{}, &models.Transaction{})
	if err != nil {
		log.Fatal("Failed to migrate database:", err)
	}

	// Initialize repositories
	accountRepo := repository.NewAccountRepository(db)
	operationTypeRepo := repository.NewOperationTypeRepository(db)
	transactionRepo := repository.NewTransactionRepository(db)

	// Initialize services
	accountService := services.NewAccountService(accountRepo)
	transactionService := services.NewTransactionService(transactionRepo, accountRepo, operationTypeRepo)

	// Initialize handlers
	accountHandler := handlers.NewAccountHandler(accountService)
	transactionHandler := handlers.NewTransactionHandler(transactionService)

	// Initialize operation types if they don't exist
	initializeOperationTypes(operationTypeRepo)

	// Setup router
	router := setupRouter(accountHandler, transactionHandler)

	// Start server
	log.Printf("Server starting on port %s", cfg.Port)
	if err := router.Run(":" + cfg.Port); err != nil {
		log.Fatal("Failed to start server:", err)
	}
}

func setupRouter(accountHandler *handlers.AccountHandler, transactionHandler *handlers.TransactionHandler) *gin.Engine {
	router := gin.Default()

	// Add middleware
	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	// Health check endpoint
	router.GET("/health", func(c *gin.Context) {
		c.JSON(200, gin.H{"status": "ok"})
	})

	// API routes
	v1 := router.Group("/api/v1")
	{
		// Account routes
		accounts := v1.Group("/accounts")
		{
			accounts.POST("", accountHandler.CreateAccount)
			accounts.GET("/:id", accountHandler.GetAccount)
		}

		// Transaction routes
		transactions := v1.Group("/transactions")
		{
			transactions.POST("", transactionHandler.CreateTransaction)
		}
	}

	// Swagger documentation
	router.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	return router
}

func initializeOperationTypes(repo *repository.OperationTypeRepository) {
	operationTypes := []models.OperationType{
		{Description: "Normal Purchase"},
		{Description: "Purchase with installments"},
		{Description: "Withdrawal"},
		{Description: "Credit Voucher"},
	}

	for _, opType := range operationTypes {
		repo.CreateIfNotExists(&opType)
	}
}
