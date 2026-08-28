export default function Table({ columns, rows, renderActions }) {
  if (!rows || rows.length === 0) {
    return <div className="empty">No records found.</div>
  }

  return (
    <table>
      <thead>
        <tr>
          {columns.map((c) => (
            <th key={c.key}>{c.label}</th>
          ))}
          {renderActions && <th>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {rows.map((row, i) => (
          <tr key={row.id ?? i}>
            {columns.map((c) => (
              <td key={c.key}>{c.render ? c.render(row) : row[c.key]}</td>
            ))}
            {renderActions && <td className="actions">{renderActions(row)}</td>}
          </tr>
        ))}
      </tbody>
    </table>
  )
}
