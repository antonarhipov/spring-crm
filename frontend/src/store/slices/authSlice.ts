import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit'
import { UserRole } from '@models'

interface AuthState {
  token: string | null
  user: {
    id: number | null
    username: string | null
    email: string | null
    roles: UserRole[]
  }
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
}

const initialState: AuthState = {
  token: localStorage.getItem('token'),
  user: {
    id: null,
    username: null,
    email: null,
    roles: []
  },
  isAuthenticated: !!localStorage.getItem('token'),
  isLoading: false,
  error: null
}

export const login = createAsyncThunk(
  'auth/login',
  async (credentials: { username: string; password: string }) => {
    const response = await fetch('/api/v1/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(credentials)
    })

    if (!response.ok) {
      throw new Error('Invalid credentials')
    }

    const data = await response.json()
    localStorage.setItem('token', data.token)
    return data
  }
)

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logout: (state) => {
      localStorage.removeItem('token')
      state.token = null
      state.user = {
        id: null,
        username: null,
        email: null,
        roles: []
      }
      state.isAuthenticated = false
    },
    setUser: (state, action: PayloadAction<{ id: number; username: string; email: string; roles: UserRole[] }>) => {
      state.user = action.payload
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.isLoading = true
        state.error = null
      })
      .addCase(login.fulfilled, (state, action) => {
        state.isLoading = false
        state.token = action.payload.token
        state.isAuthenticated = true
      })
      .addCase(login.rejected, (state, action) => {
        state.isLoading = false
        state.error = action.error.message || 'Login failed'
      })
  }
})

export const { logout, setUser } = authSlice.actions
export default authSlice.reducer
