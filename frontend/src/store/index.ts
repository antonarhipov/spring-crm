import { configureStore } from '@reduxjs/toolkit'
import authReducer from './slices/authSlice'
import customerReducer from './slices/customerSlice'
import contactReducer from './slices/contactSlice'
import userReducer from './slices/userSlice'

export const store = configureStore({
  reducer: {
    auth: authReducer,
    customers: customerReducer,
    contacts: contactReducer,
    users: userReducer
  }
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch