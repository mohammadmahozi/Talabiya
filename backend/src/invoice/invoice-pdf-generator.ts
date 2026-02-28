import PDFDocument from 'pdfkit'
import type { Invoice, OrderItem } from './invoice'

const Colors = {
  text: '#1a1a1a',
  subtext: '#666666',
  primary: '#A30505',
  border: '#e0e0e0',
}

const Margins = { top: 72, left: 72, right: 72, bottom: 72 }

export function generateInvoicePdf(invoice: Invoice): Promise<Buffer> {
  return new Promise((resolve, reject) => {
    const doc = new PDFDocument({ size: 'A4', margin: 72 })
    const chunks: Buffer[] = []

    doc.on('data', (chunk) => chunks.push(chunk))
    doc.on('end', () => resolve(Buffer.concat(chunks)))
    doc.on('error', reject)

    drawHeader(doc, invoice)
    drawUserInfo(doc, invoice)
    drawSummary(doc, invoice)
    drawOrders(doc, invoice)
    drawFooter(doc)

    doc.end()
  })
}

function drawHeader(doc: PDFKit.PDFDocument, invoice: Invoice) {
  // App name
  doc
    .fontSize(24)
    .fillColor(Colors.primary)
    .text('TalabiyaApp', Margins.left, Margins.top, { continued: true })

  // Invoice label on the right
  doc
    .fontSize(24)
    .fillColor(Colors.text)
    .text('Invoice', { align: 'right' })

  doc.moveDown(0.5)
  drawHorizontalLine(doc, doc.y)
  doc.moveDown()
}

function drawUserInfo(doc: PDFKit.PDFDocument, invoice: Invoice) {
  doc
    .fontSize(12)
    .fillColor(Colors.subtext)
    .text('Prepared for', Margins.left)

  doc
    .fontSize(16)
    .fillColor(Colors.text)
    .text(invoice.user.name)

  doc.moveDown(0.3)

  doc
    .fontSize(10)
    .fillColor(Colors.subtext)
    .text(`Period: ${invoice.summary.from} to ${invoice.summary.to}`)
    .text(`Orders Placed: ${invoice.summary.ordersPlaced}   Orders Covered: ${invoice.summary.ordersCovered}`)

  doc.moveDown()
  drawHorizontalLine(doc, doc.y)
  doc.moveDown()
}

function drawSummary(doc: PDFKit.PDFDocument, invoice: Invoice) {
  doc
    .fontSize(14)
    .fillColor(Colors.text)
    .text('Summary')

  doc.moveDown(0.5)

  const summaryItems = [
    { label: 'Total Paid', value: `${invoice.totals.totalPaid} SAR` },
    { label: 'Total Owed', value: `${invoice.totals.totalOwed} SAR` },
    { label: 'Final Total', value: `${invoice.totals.finalTotal} SAR (${invoice.totals.direction})` },
  ]

  summaryItems.forEach(({ label, value }) => {
    doc
      .fontSize(11)
      .fillColor(Colors.subtext)
      .text(label, Margins.left, doc.y, { continued: true })
      .fillColor(Colors.text)
      .text(value, { align: 'right' })
    doc.moveDown(0.3)
  })

  doc.moveDown()
  drawHorizontalLine(doc, doc.y)
  doc.moveDown()
}

function drawOrders(doc: PDFKit.PDFDocument, invoice: Invoice) {
  doc
    .fontSize(14)
    .fillColor(Colors.text)
    .text('Order Breakdown')

  doc.moveDown(0.5)

  invoice.orders.forEach((order) => {
    // Restaurant name
    doc
      .fontSize(12)
      .fillColor(Colors.text)
      .text(order.restaurant)
      .fontSize(9)
      .fillColor(Colors.subtext)
      .text(`${order.date}`)

    doc.moveDown(0.3)

    if (order.items.length > 0) {
      drawOrderItems(doc, order.items)
    }

    // Order totals
    if (order.totalPaid > 0) {
      doc
        .fontSize(10)
        .fillColor(Colors.subtext)
        .text('Total paid', Margins.left, doc.y, { continued: true })
        .fillColor(Colors.primary)
        .text(`${order.totalPaid} SAR`, { align: 'right' })
      doc.moveDown(0.3)
    }

    if (order.totalOwed > 0) {
      doc
        .fontSize(10)
        .fillColor(Colors.subtext)
        .text('Total owed', Margins.left, doc.y, { continued: true })
        .fillColor(Colors.primary)
        .text(`${order.totalOwed} SAR`, { align: 'right' })
    }

    doc.moveDown()

    doc.moveDown()
  })
}

function drawOrderItems(doc: PDFKit.PDFDocument, items: Array<OrderItem>) {
  // Items header
  const y = doc.y
  const columns = itemColumns(doc.page.width)

  doc
    .fontSize(9)
    .fillColor(Colors.subtext)
    .text('ITEM', columns.item.x, y)
    .text('QTY', columns.quantity.x, y, { width: columns.quantity.width, align: 'right' })
    .text('UNIT PRICE', columns.unitPrice.x, y, { width: columns.unitPrice.width, align: 'right' })
    .text('TOTAL', columns.total.x, y, { width: columns.total.width, align: 'right' })

  doc.moveDown(0.5)
  drawHorizontalLine(doc, doc.y)
  doc.moveDown(0.5)

  // Items
  items.forEach((item) => {
    const y = doc.y
    const name = truncateText(doc, item.name, columns.item.width)
    doc
      .fontSize(11)
      .fillColor(Colors.subtext)
      .text(name, columns.item.x, y)
      .text(`x${item.quantity}`, columns.quantity.x, y, { width: columns.quantity.width, align: 'right' })
      .text(`${item.price} SAR`, columns.unitPrice.x, y, { width: columns.unitPrice.width, align: 'right' })
      .text(`${item.total} SAR`, columns.total.x, y, { width: columns.total.width, align: 'right' })
    doc.moveDown(0.3)
  })

  doc.moveDown(0.3)
}

function itemColumns(pageWidth: number) {
  const safeWidth = pageWidth - Margins.left - Margins.right
  const itemWidth = safeWidth * 0.5
  const quantityWidth = safeWidth * 0.1
  const unitPriceWidth = safeWidth * 0.2
  const totalWidth = safeWidth * 0.2

  const totalX = pageWidth - Margins.right - totalWidth
  const unitPriceX = totalX - unitPriceWidth
  const quantityX = unitPriceX - quantityWidth
  const itemX = Margins.left

  return {
    item: { x: itemX, width: itemWidth },
    quantity: { x: quantityX, width: quantityWidth },
    unitPrice: { x: unitPriceX, width: unitPriceWidth },
    total: { x: totalX, width: totalWidth },
  }
}

function truncateText(doc: PDFKit.PDFDocument, text: string, maxWidth: number): string {
  if (doc.widthOfString(text) <= maxWidth) return text
  let truncated = text
  while (doc.widthOfString(truncated + '...') > maxWidth) {
    truncated = truncated.slice(0, -1)
  }
  return truncated + '...'
}

function drawFooter(doc: PDFKit.PDFDocument) {
  doc
    .fontSize(9)
    .fillColor(Colors.subtext)
    .text(`Generated on ${new Date().toLocaleDateString()}`, Margins.left, doc.y, { align: 'center' })
}

function drawHorizontalLine(doc: PDFKit.PDFDocument, y: number) {
  doc
    .moveTo(Margins.left, y)
    .lineTo(doc.page.width - Margins.right, y)
    .strokeColor(Colors.border)
    .stroke()
}