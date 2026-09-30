package com.example.testbankapp.domain.usecase

import com.example.testbankapp.core.Resource
import com.example.testbankapp.data.dto.TransactionResponse
import com.example.testbankapp.data.dto.TransactionStatus
import com.example.testbankapp.data.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DepositFundsUseCaseTest {

    private val repository: AccountRepository = mockk()
    private lateinit var useCase: DepositFundsUseCase

    @BeforeEach
    fun setUp() {
        useCase = DepositFundsUseCase(repository)
    }

    @Test
    fun `when amount is zero or negative, returns Resource Error and does not call repository`() =
        runTest {
            // Given
            val invalidAmount = BigDecimal("0.00")
            val accountNumber = "ACC123456"

            // When
            val result = useCase(accountNumber, invalidAmount)

            // Then
            Assertions.assertTrue(result is Resource.Error)
            Assertions.assertEquals(
                "Amount must be greater than zero",
                (result as Resource.Error).message
            )

            coVerify(exactly = 0) { repository.deposit(any(), any(), any()) }
        }

    @Test
    fun `when deposit is successful, maps DTO to TransactionResult and returns Resource Success`() =
        runTest {
            // Given
            val accountNumber = "ACC123456"
            val amount = BigDecimal("100.00")

            val mockDto = TransactionResponse(
                transactionId = 12345,
                status = TransactionStatus.SUCCESS,
                amount = amount,
                currency = "USD"
            )

            coEvery { repository.deposit(accountNumber, amount, any()) } returns Resource.Success(
                mockDto
            )

            // When
            val result = useCase(accountNumber, amount)

            // Then
            Assertions.assertTrue(result is Resource.Success)
            val data = (result as Resource.Success).data

            Assertions.assertEquals(12345, data?.transactionId)
            Assertions.assertEquals("SUCCESS", data?.status)
            Assertions.assertEquals(amount, data?.amount)
            Assertions.assertEquals("USD", data?.currency)

            // Verify the method was called once
            coVerify(exactly = 1) { repository.deposit(accountNumber, amount, any()) }
        }

    @Test
    fun `when repository returns Error, useCase forwards Resource Error`() = runTest {
        // Given
        val accountNumber = "ACC123456"
        val amount = BigDecimal("50.00")
        val errorMessage = "Account not found"

        coEvery { repository.deposit(accountNumber, amount, any()) } returns Resource.Error(
            errorMessage
        )

        // When
        val result = useCase(accountNumber, amount)

        // Then
        Assertions.assertTrue(result is Resource.Error)
        Assertions.assertEquals(errorMessage, (result as Resource.Error).message)
    }
}