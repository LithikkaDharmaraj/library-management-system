export default function Alert({ type, message, onClose }) {
  if (!message) return null
  return (
    <div className={`alert ${type}`}>
      {message}
      {onClose && (
        <button type="button" onClick={onClose} style={{ float: 'right', border: 'none', background: 'none', cursor: 'pointer', fontWeight: 'bold' }}>
          ×
        </button>
      )}
    </div>
  )
}
