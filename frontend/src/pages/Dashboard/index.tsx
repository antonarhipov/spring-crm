import React from 'react'
import { Card, Row, Col, Statistic, Typography } from 'antd'
import { TeamOutlined, UserOutlined, ShopOutlined, RiseOutlined } from '@ant-design/icons'
import styled from 'styled-components'
import { useSelector } from 'react-redux'
import { RootState } from '@store/index'
import { UserRole } from '@models'

const { Title } = Typography

const PageHeader = styled.div`
  margin-bottom: 24px;
`

const StyledCard = styled(Card)`
  height: 100%;
  .ant-card-body {
    height: 100%;
  }
`

const StatisticCard = styled(Card)`
  .ant-statistic-title {
    font-size: 16px;
    margin-bottom: 16px;
  }
  .ant-statistic-content-value {
    font-size: 24px;
    font-weight: 600;
  }
`

const Dashboard: React.FC = () => {
  const { user } = useSelector((state: RootState) => state.auth)
  const isAdmin = user.roles.includes(UserRole.ADMIN)

  return (
    <div>
      <PageHeader>
        <Title level={2}>Dashboard</Title>
        <Typography.Text type="secondary">
          Welcome back, {user.username}!
        </Typography.Text>
      </PageHeader>

      <Row gutter={[24, 24]}>
        <Col xs={24} sm={12} lg={6}>
          <StatisticCard>
            <Statistic
              title="Total Customers"
              value={156}
              prefix={<TeamOutlined />}
              valueStyle={{ color: '#2196F3' }}
            />
          </StatisticCard>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatisticCard>
            <Statistic
              title="Active Customers"
              value={124}
              prefix={<ShopOutlined />}
              valueStyle={{ color: '#4CAF50' }}
            />
          </StatisticCard>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatisticCard>
            <Statistic
              title="New Leads"
              value={32}
              prefix={<RiseOutlined />}
              valueStyle={{ color: '#FFC107' }}
            />
          </StatisticCard>
        </Col>
        {isAdmin && (
          <Col xs={24} sm={12} lg={6}>
            <StatisticCard>
              <Statistic
                title="Total Users"
                value={12}
                prefix={<UserOutlined />}
                valueStyle={{ color: '#9C27B0' }}
              />
            </StatisticCard>
          </Col>
        )}
      </Row>

      <Row gutter={[24, 24]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={16}>
          <StyledCard title="Recent Activity">
            {/* TODO: Add activity timeline component */}
            <p>Recent customer interactions will be displayed here.</p>
          </StyledCard>
        </Col>
        <Col xs={24} lg={8}>
          <StyledCard title="Quick Actions">
            {/* TODO: Add quick action buttons */}
            <p>Common actions will be available here.</p>
          </StyledCard>
        </Col>
      </Row>
    </div>
  )
}

export default Dashboard
