import styled from 'styled-components'
import { Typography } from 'antd'

const { Title } = Typography

interface CardProps {
  title?: React.ReactNode
  children: React.ReactNode
  footer?: React.ReactNode
  className?: string
}

const CardWrapper = styled.div`
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  border: 1px solid var(--neutral-200);
`

const CardHeader = styled.div`
  padding: 16px 24px;
  border-bottom: 1px solid var(--neutral-200);
  
  h3 {
    margin: 0;
    color: var(--neutral-900);
    font-size: var(--font-size-lg);
    font-weight: 600;
  }
`

const CardContent = styled.div`
  padding: 24px;
`

const CardFooter = styled.div`
  padding: 16px 24px;
  border-top: 1px solid var(--neutral-200);
  background-color: var(--neutral-50);
  border-radius: 0 0 8px 8px;
  
  display: flex;
  justify-content: flex-end;
  gap: 12px;
`

export const Card = ({ title, children, footer, className }: CardProps) => {
  return (
    <CardWrapper className={className}>
      {title && (
        <CardHeader>
          {typeof title === 'string' ? (
            <Title level={3}>{title}</Title>
          ) : (
            title
          )}
        </CardHeader>
      )}
      <CardContent>{children}</CardContent>
      {footer && <CardFooter>{footer}</CardFooter>}
    </CardWrapper>
  )
}

export default Card