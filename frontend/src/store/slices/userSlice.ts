import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { User, UserRole } from '@models'


interface UserState {
  items: User[]
  selectedUser: User | null
  totalItems: number
  currentPage: number
  pageSize: number
  isLoading: boolean
  error: string | null
}

const initialState: UserState = {
  items: [],
  selectedUser: null,
  totalItems: 0,
  currentPage: 0,
  pageSize: 20,
  isLoading: false,
  error: null
}

export const fetchUsers = createAsyncThunk(
  'users/fetchUsers',
  async ({ page = 0, size = 20 }: { page?: number; size?: number }) => {
    const response = await fetch(`/api/v1/users?page=${page}&size=${size}`)
    if (!response.ok) {
      throw new Error('Failed to fetch users')
    }
    return response.json()
  }
)

export interface CreateUserRequest {
  username: string
  email: string
  password: string
  roles: UserRole[]
}

export const createUser = createAsyncThunk(
  'users/createUser',
  async (user: { username: string; email: string; password: string; roles: UserRole[] }) => {
    const response = await fetch('/api/v1/users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(user)
    })
    if (!response.ok) {
      throw new Error('Failed to create user')
    }
    return response.json()
  }
)

export const updateUser = createAsyncThunk(
  'users/updateUser',
  async ({ id, user }: { id: number; user: Partial<User> }) => {
    const response = await fetch(`/api/v1/users/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(user)
    })
    if (!response.ok) {
      throw new Error('Failed to update user')
    }
    return response.json()
  }
)

export const deleteUser = createAsyncThunk(
  'users/deleteUser',
  async (id: number) => {
    const response = await fetch(`/api/v1/users/${id}`, {
      method: 'DELETE'
    })
    if (!response.ok) {
      throw new Error('Failed to delete user')
    }
    return id
  }
)

const userSlice = createSlice({
  name: 'users',
  initialState,
  reducers: {
    setSelectedUser: (state, action: PayloadAction<User | null>) => {
      state.selectedUser = action.payload
    },
    setPageSize: (state, action: PayloadAction<number>) => {
      state.pageSize = action.payload
    }
  },
  extraReducers: (builder) => {
    builder
      // Fetch users
      .addCase(fetchUsers.pending, (state) => {
        state.isLoading = true
        state.error = null
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.isLoading = false
        state.items = action.payload.content
        state.totalItems = action.payload.totalElements
        state.currentPage = action.payload.number
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.isLoading = false
        state.error = action.error.message || 'Failed to fetch users'
      })
      // Create user
      .addCase(createUser.fulfilled, (state, action) => {
        state.items.push(action.payload)
        state.totalItems += 1
      })
      // Update user
      .addCase(updateUser.fulfilled, (state, action) => {
        const index = state.items.findIndex(item => item.id === action.payload.id)
        if (index !== -1) {
          state.items[index] = action.payload
        }
        if (state.selectedUser?.id === action.payload.id) {
          state.selectedUser = action.payload
        }
      })
      // Delete user
      .addCase(deleteUser.fulfilled, (state, action) => {
        state.items = state.items.filter(item => item.id !== action.payload)
        state.totalItems -= 1
        if (state.selectedUser?.id === action.payload) {
          state.selectedUser = null
        }
      })
  }
})

export const { setSelectedUser, setPageSize } = userSlice.actions
export default userSlice.reducer
