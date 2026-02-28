export interface Invoice {
  readonly user: User
  readonly summary: Summary
  readonly totals: Totals
  readonly orders: Order[]
}

export interface User {
  readonly id: number
  readonly name: string
  readonly email: string
}

export interface Summary {
  readonly from: string
  readonly to: string
  readonly ordersPlaced: number
  readonly ordersCovered: number
}

export interface Totals {
  readonly totalPaid: number
  readonly totalOwed: number
  readonly finalTotal: number
  readonly direction: Direction
}

export type Direction = 'Credit' | 'Debit'

export interface Order {
  readonly id: number
  readonly restaurant: string
  readonly date: string
  readonly totalPaid: number
  readonly totalOwed: number
  readonly items: OrderItem[]
}

export interface OrderItem {
  readonly id: number
  readonly name: string
  readonly price: number
  readonly quantity: number
  readonly total: number
}