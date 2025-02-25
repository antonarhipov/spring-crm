import React from 'react'
import { Form, Input, Select, Button } from 'antd'
import styled from 'styled-components'

const { Option } = Select

const FormActions = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 24px;
`

import { UserFormData, UserRole } from '@models'

interface UserFormProps {
  initialValues?: Partial<UserFormData>
  onSubmit: (values: UserFormData) => void
  onCancel: () => void
  isLoading?: boolean
  isEdit?: boolean
}

const UserForm: React.FC<UserFormProps> = ({
  initialValues,
  onSubmit,
  onCancel,
  isLoading = false,
  isEdit = false
}) => {
  const [form] = Form.useForm<UserFormData>()

  const handleSubmit = async (values: UserFormData) => {
    try {
      await form.validateFields()
      onSubmit(values)
    } catch (error) {
      console.error('Validation failed:', error)
    }
  }

  return (
    <Form
      form={form}
      layout="vertical"
      initialValues={initialValues}
      onFinish={handleSubmit}
    >
      <Form.Item
        label="Username"
        name="username"
        rules={[
          { required: true, message: 'Please enter username' },
          { min: 3, message: 'Username must be at least 3 characters' }
        ]}
      >
        <Input placeholder="Enter username" />
      </Form.Item>

      <Form.Item
        label="Email"
        name="email"
        rules={[
          { required: true, message: 'Please enter email' },
          { type: 'email', message: 'Please enter a valid email' }
        ]}
      >
        <Input placeholder="Enter email" />
      </Form.Item>

      {!isEdit && (
        <Form.Item
          label="Password"
          name="password"
          rules={[
            { required: !isEdit, message: 'Please enter password' },
            { min: 6, message: 'Password must be at least 6 characters' }
          ]}
        >
          <Input.Password placeholder="Enter password" />
        </Form.Item>
      )}

      <Form.Item
        label="Roles"
        name="roles"
        rules={[{ required: true, message: 'Please select at least one role' }]}
      >
        <Select
          mode="multiple"
          placeholder="Select roles"
          style={{ width: '100%' }}
        >
          <Option value={UserRole.ADMIN}>Admin</Option>
          <Option value={UserRole.SALES_MANAGER}>Sales Manager</Option>
          <Option value={UserRole.SALES_REPRESENTATIVE}>Sales Representative</Option>
          <Option value={UserRole.READ_ONLY}>Read Only</Option>
        </Select>
      </Form.Item>

      <FormActions>
        <Button onClick={onCancel}>
          Cancel
        </Button>
        <Button type="primary" htmlType="submit" loading={isLoading}>
          {isEdit ? 'Update User' : 'Create User'}
        </Button>
      </FormActions>
    </Form>
  )
}

export default UserForm
