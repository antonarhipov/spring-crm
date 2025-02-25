import React from 'react'
import { Card, Form, Input, Select, Switch, Button, Tabs, Typography } from 'antd'
import { useSelector } from 'react-redux'
import styled from 'styled-components'
import { RootState } from '@store/index'
import { UserRole } from '@models'

const { Title } = Typography
const { TabPane } = Tabs
const { Option } = Select

const PageHeader = styled.div`
  margin-bottom: 24px;
`

const FormSection = styled.div`
  margin-bottom: 32px;

  .ant-form-item:last-child {
    margin-bottom: 0;
  }
`

const SectionTitle = styled(Title)`
  font-size: 18px !important;
  margin-bottom: 16px !important;
`

const Settings: React.FC = () => {
  const { user } = useSelector((state: RootState) => state.auth)
  const isAdmin = user.roles.includes(UserRole.ADMIN)

  const handleUpdateProfile = (values: any) => {
    // TODO: Implement profile update
    console.log('Update profile:', values)
  }

  const handleUpdatePreferences = (values: any) => {
    // TODO: Implement preferences update
    console.log('Update preferences:', values)
  }

  const handleUpdateSystemSettings = (values: any) => {
    // TODO: Implement system settings update
    console.log('Update system settings:', values)
  }

  return (
    <div>
      <PageHeader>
        <Title level={2}>Settings</Title>
      </PageHeader>

      <Tabs defaultActiveKey="profile">
        <TabPane tab="Profile" key="profile">
          <Card>
            <Form
              layout="vertical"
              initialValues={{
                username: user.username,
                email: user.email
              }}
              onFinish={handleUpdateProfile}
            >
              <FormSection>
                <SectionTitle level={4}>Personal Information</SectionTitle>
                <Form.Item
                  label="Username"
                  name="username"
                  rules={[{ required: true, message: 'Please input your username!' }]}
                >
                  <Input />
                </Form.Item>
                <Form.Item
                  label="Email"
                  name="email"
                  rules={[
                    { required: true, message: 'Please input your email!' },
                    { type: 'email', message: 'Please enter a valid email!' }
                  ]}
                >
                  <Input />
                </Form.Item>
              </FormSection>

              <FormSection>
                <SectionTitle level={4}>Change Password</SectionTitle>
                <Form.Item
                  label="Current Password"
                  name="currentPassword"
                  rules={[{ required: true, message: 'Please input your current password!' }]}
                >
                  <Input.Password />
                </Form.Item>
                <Form.Item
                  label="New Password"
                  name="newPassword"
                  rules={[{ required: true, message: 'Please input your new password!' }]}
                >
                  <Input.Password />
                </Form.Item>
                <Form.Item
                  label="Confirm Password"
                  name="confirmPassword"
                  dependencies={['newPassword']}
                  rules={[
                    { required: true, message: 'Please confirm your password!' },
                    ({ getFieldValue }) => ({
                      validator(_, value) {
                        if (!value || getFieldValue('newPassword') === value) {
                          return Promise.resolve()
                        }
                        return Promise.reject(new Error('The passwords do not match!'))
                      }
                    })
                  ]}
                >
                  <Input.Password />
                </Form.Item>
              </FormSection>

              <Button type="primary" htmlType="submit">
                Update Profile
              </Button>
            </Form>
          </Card>
        </TabPane>

        <TabPane tab="Preferences" key="preferences">
          <Card>
            <Form
              layout="vertical"
              onFinish={handleUpdatePreferences}
            >
              <FormSection>
                <SectionTitle level={4}>Display Settings</SectionTitle>
                <Form.Item
                  label="Theme"
                  name="theme"
                  initialValue="light"
                >
                  <Select>
                    <Option value="light">Light</Option>
                    <Option value="dark">Dark</Option>
                  </Select>
                </Form.Item>
                <Form.Item
                  label="Items per page"
                  name="pageSize"
                  initialValue={20}
                >
                  <Select>
                    <Option value={10}>10</Option>
                    <Option value={20}>20</Option>
                    <Option value={50}>50</Option>
                    <Option value={100}>100</Option>
                  </Select>
                </Form.Item>
              </FormSection>

              <FormSection>
                <SectionTitle level={4}>Notifications</SectionTitle>
                <Form.Item
                  label="Email Notifications"
                  name="emailNotifications"
                  valuePropName="checked"
                  initialValue={true}
                >
                  <Switch />
                </Form.Item>
              </FormSection>

              <Button type="primary" htmlType="submit">
                Save Preferences
              </Button>
            </Form>
          </Card>
        </TabPane>

        {isAdmin && (
          <TabPane tab="System" key="system">
            <Card>
              <Form
                layout="vertical"
                onFinish={handleUpdateSystemSettings}
              >
                <FormSection>
                  <SectionTitle level={4}>System Settings</SectionTitle>
                  <Form.Item
                    label="Company Name"
                    name="companyName"
                    rules={[{ required: true }]}
                  >
                    <Input />
                  </Form.Item>
                  <Form.Item
                    label="Support Email"
                    name="supportEmail"
                    rules={[{ required: true, type: 'email' }]}
                  >
                    <Input />
                  </Form.Item>
                </FormSection>

                <Button type="primary" htmlType="submit">
                  Save System Settings
                </Button>
              </Form>
            </Card>
          </TabPane>
        )}
      </Tabs>
    </div>
  )
}

export default Settings
