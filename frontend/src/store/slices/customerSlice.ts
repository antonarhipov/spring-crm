import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

interface Customer {
  id: number
  name: string
  email: string
  phone: string
  status: 'ACTIVE' | 'INACTIVE' | 'LEAD'
  category: 'REGULAR' | 'VIP' | 'PREMIUM'
  createdBy: number
  createdAt: string
  updatedAt: string
}

interface CustomerState {
  items: Customer[]
  selectedCustomer: Customer | null
  totalItems: number
  currentPage: number
  pageSize: number
  isLoading: boolean
  error: string | null
}

const initialState: CustomerState = {
  items: [],
  selectedCustomer: null,
  totalItems: 0,
  currentPage: 0,
  pageSize: 20,
  isLoading: false,
  error: null
}

export const fetchCustomers = createAsyncThunk(
  'customers/fetchCustomers',
  async ({ page = 0, size = 20 }: { page?: number; size?: number }) => {
    const response = await fetch(`/api/v1/customers?page=${page}&size=${size}`)
    if (!response.ok) {
      throw new Error('Failed to fetch customers')
    }
    return response.json()
  }
)

export const createCustomer = createAsyncThunk(
  'customers/createCustomer',
  async (customer: Omit<Customer, 'id' | 'createdBy' | 'createdAt' | 'updatedAt'>) => {
    const response = await fetch('/api/v1/customers', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(customer)
    })
    if (!response.ok) {
      throw new Error('Failed to create customer')
    }
    return response.json()
  }
)

export const updateCustomer = createAsyncThunk(
  'customers/updateCustomer',
  async ({ id, customer }: { id: number; customer: Partial<Customer> }) => {
    const response = await fetch(`/api/v1/customers/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(customer)
    })
    if (!response.ok) {
      throw new Error('Failed to update customer')
    }
    return response.json()
  }
)

export const deleteCustomer = createAsyncThunk(
  'customers/deleteCustomer',
  async (id: number) => {
    const response = await fetch(`/api/v1/customers/${id}`, {
      method: 'DELETE'
    })
    if (!response.ok) {
      throw new Error('Failed to delete customer')
    }
    return id
  }
)

const customerSlice = createSlice({
  name: 'customers',
  initialState,
  reducers: {
    setSelectedCustomer: (state, action: PayloadAction<Customer | null>) => {
      state.selectedCustomer = action.payload
    },
    setPageSize: (state, action: PayloadAction<number>) => {
      state.pageSize = action.payload
    }
  },
  extraReducers: (builder) => {
    builder
      // Fetch customers
      .addCase(fetchCustomers.pending, (state) => {
        state.isLoading = true
        state.error = null
      })
      .addCase(fetchCustomers.fulfilled, (state, action) => {
        state.isLoading = false
        state.items = action.payload.content
        state.totalItems = action.payload.totalElements
        state.currentPage = action.payload.number
      })
      .addCase(fetchCustomers.rejected, (state, action) => {
        state.isLoading = false
        state.error = action.error.message || 'Failed to fetch customers'
      })
      // Create customer
      .addCase(createCustomer.fulfilled, (state, action) => {
        state.items.push(action.payload)
        state.totalItems += 1
      })
      // Update customer
      .addCase(updateCustomer.fulfilled, (state, action) => {
        const index = state.items.findIndex(item => item.id === action.payload.id)
        if (index !== -1) {
          state.items[index] = action.payload
        }
        if (state.selectedCustomer?.id === action.payload.id) {
          state.selectedCustomer = action.payload
        }
      })
      // Delete customer
      .addCase(deleteCustomer.fulfilled, (state, action) => {
        state.items = state.items.filter(item => item.id !== action.payload)
        state.totalItems -= 1
        if (state.selectedCustomer?.id === action.payload) {
          state.selectedCustomer = null
        }
      })
  }
})

export const { setSelectedCustomer, setPageSize } = customerSlice.actions
export default customerSlice.reducer