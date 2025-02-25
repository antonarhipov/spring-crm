import React from 'react'
import { Form, Input, Button, Card, Typography, Alert } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components'
import { login } from '@store/slices/authSlice'
import type { RootState } from '@store/index'
import type { AppDispatch } from '@store/index'

const { Title } = Typography

const LoginContainer = styled.div`
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f2f5;
`

const LoginCard = styled(Card)`
  width: 100%;
  max-width: 400px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
`

const LogoContainer = styled.div`
  text-align: center;
  margin-bottom: 24px;

  img {
    height: 48px;
  }
`

const LoginTitle = styled(Title)`
  text-align: center;
  margin-bottom: 32px !important;
`

interface LoginForm {
  username: string
  password: string
}

const Login: React.FC = () => {
  const navigate = useNavigate()
  const dispatch = useDispatch<AppDispatch>()
  const { isLoading, error } = useSelector((state: RootState) => state.auth)

  const onFinish = async (values: LoginForm) => {
    try {
      await dispatch(login(values)).unwrap()
      navigate('/')
    } catch (err) {
      // Error is handled by the auth slice
    }
  }

  return (
    <LoginContainer>
      <LoginCard>
        <LogoContainer>
          <img src="/logo.svg" alt="CRM Logo" />
        </LogoContainer>
        <LoginTitle level={2}>Welcome to CRM</LoginTitle>
        {error && (
          <Alert
            message="Error"
            description={error}
            type="error"
            showIcon
            style={{ marginBottom: 24 }}
          />
        )}
        <Form
          name="login"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          size="large"
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: 'Please input your username!' }]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="Username"
              autoComplete="username"
            />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Please input your password!' }]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Password"
              autoComplete="current-password"
            />
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              loading={isLoading}
            >
              Log in
            </Button>
          </Form.Item>
        </Form>
      </LoginCard>
    </LoginContainer>
  )
}

export default Login