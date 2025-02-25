import { Card } from '@/components/common'
import styled from 'styled-components'

const DashboardGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
`

const StatCard = styled(Card)`
  text-align: center;
`

const StatValue = styled.div`
  font-size: var(--font-size-2xl);
  font-weight: 700;
  color: var(--primary-500);
  margin-bottom: 8px;
`

const StatLabel = styled.div`
  font-size: var(--font-size-sm);
  color: var(--neutral-600);
`

export const Dashboard = () => {
  return (
    <div>
      <h1>Dashboard</h1>
      <DashboardGrid>
        <StatCard>
          <StatValue>128</StatValue>
          <StatLabel>Total Customers</StatLabel>
        </StatCard>
        <StatCard>
          <StatValue>64</StatValue>
          <StatLabel>Active Orders</StatLabel>
        </StatCard>
        <StatCard>
          <StatValue>$12,480</StatValue>
          <StatLabel>Monthly Revenue</StatLabel>
        </StatCard>
      </DashboardGrid>
      <Card title="Recent Activity">
        {/* Add activity list here */}
        <p>No recent activity</p>
      </Card>
    </div>
  )
}

export default Dashboard