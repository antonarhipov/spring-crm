import styled, { css } from 'styled-components'
import { Button as AntButton } from 'antd'
import type { ButtonProps } from 'antd'

interface StyledButtonProps extends ButtonProps {
  variant?: 'primary' | 'secondary' | 'text'
}

const getButtonStyles = (variant: StyledButtonProps['variant'] = 'primary') => {
  switch (variant) {
    case 'secondary':
      return css`
        background-color: white;
        color: var(--primary-500);
        border: 1px solid var(--primary-500);
        &:hover {
          background-color: var(--primary-50);
          border-color: var(--primary-700);
          color: var(--primary-700);
        }
      `
    case 'text':
      return css`
        background-color: transparent;
        color: var(--primary-500);
        border: none;
        padding: 4px 8px;
        height: auto;
        &:hover {
          background-color: var(--primary-50);
          color: var(--primary-700);
        }
      `
    default:
      return css`
        background-color: var(--primary-500);
        color: white;
        border: none;
        &:hover {
          background-color: var(--primary-700);
        }
      `
  }
}

const StyledButton = styled(AntButton)<StyledButtonProps>`
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: 500;
  height: 40px;
  transition: all 0.2s;
  
  &:focus {
    outline: 2px solid var(--primary-500);
    outline-offset: 2px;
  }
  
  ${props => getButtonStyles(props.variant)}
  
  &:disabled {
    background-color: var(--neutral-200);
    color: var(--neutral-500);
    border: none;
    cursor: not-allowed;
  }
`

export const Button = ({ children, variant, ...props }: StyledButtonProps) => {
  return (
    <StyledButton variant={variant} {...props}>
      {children}
    </StyledButton>
  )
}

export default Button