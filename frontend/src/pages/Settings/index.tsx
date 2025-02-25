import { Card, Input } from '@/components/common'
import { Switch, Form } from 'antd'
import styled from 'styled-components'

const SettingSection = styled.div`
  margin-bottom: 32px;
  
  h2 {
    margin-bottom: 24px;
  }
`

const SettingRow = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding: 16px;
  background: var(--neutral-50);
  border-radius: 4px;

  .setting-label {
    h3 {
      margin: 0;
      font-size: var(--font-size-base);
      font-weight: 500;
    }
    
    p {
      margin: 4px 0 0;
      color: var(--neutral-600);
      font-size: var(--font-size-sm);
    }
  }
`

export const Settings = () => {
  return (
    <div>
      <h1>Settings</h1>
      
      <SettingSection>
        <Card title="General Settings">
          <Form layout="vertical">
            <Form.Item
              label="Company Name"
              name="companyName"
            >
              <Input placeholder="Enter company name" />
            </Form.Item>
            
            <Form.Item
              label="Email"
              name="email"
            >
              <Input type="email" placeholder="Enter email address" />
            </Form.Item>
          </Form>
        </Card>
      </SettingSection>

      <SettingSection>
        <Card title="Notifications">
          <SettingRow>
            <div className="setting-label">
              <h3>Email Notifications</h3>
              <p>Receive email notifications for new orders</p>
            </div>
            <Switch defaultChecked />
          </SettingRow>
          
          <SettingRow>
            <div className="setting-label">
              <h3>Order Updates</h3>
              <p>Get notified when order status changes</p>
            </div>
            <Switch defaultChecked />
          </SettingRow>
        </Card>
      </SettingSection>
    </div>
  )
}

export default Settings