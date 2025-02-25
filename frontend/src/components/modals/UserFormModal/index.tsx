import React from 'react'
import { Modal, message } from 'antd'
import { useDispatch } from 'react-redux'
import { AppDispatch } from '@store/index'
import { createUser, updateUser } from '@store/slices/userSlice'
import UserForm from '@components/forms/UserForm'

import { User, UserFormData } from '@models'

interface UserFormModalProps {
  visible: boolean
  initialValues?: Partial<User>
  onClose: () => void
}

const UserFormModal: React.FC<UserFormModalProps> = ({
  visible,
  initialValues,
  onClose
}) => {
  const dispatch = useDispatch<AppDispatch>()
  const [isLoading, setIsLoading] = React.useState(false)

  const handleSubmit = async (values: UserFormData & { password?: string }) => {
    setIsLoading(true)
    try {
      if (initialValues?.id) {
        await dispatch(updateUser({
          id: initialValues.id,
          user: values
        })).unwrap()
        message.success('User updated successfully')
      } else {
        if (!values.password) {
          throw new Error('Password is required for new users')
        }
        await dispatch(createUser({
          username: values.username,
          email: values.email,
          password: values.password,
          roles: values.roles
        })).unwrap()
        message.success('User created successfully')
      }
      onClose()
    } catch (error) {
      message.error('Failed to save user')
      console.error('Failed to save user:', error)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Modal
      title={initialValues ? 'Edit User' : 'Create User'}
      open={visible}
      onCancel={onClose}
      footer={null}
      width={600}
      destroyOnClose
    >
      <UserForm
        initialValues={initialValues}
        onSubmit={handleSubmit}
        onCancel={onClose}
        isLoading={isLoading}
        isEdit={!!initialValues}
      />
    </Modal>
  )
}

export default UserFormModal
