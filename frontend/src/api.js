async function request(path, method = 'GET', body = null) {
  const options = {
    method,
    headers: {}
  }
  if (body !== null) {
    options.headers['Content-Type'] = 'application/json'
    options.body = JSON.stringify(body)
  }

  const res = await fetch(path, options)

  if (res.status === 204) {
    return null
  }

  let data = null
  try {
    data = await res.json()
  } catch {
    data = null
  }

  if (!res.ok) {
    const message = (data && data.error) || `Request failed with status ${res.status}`
    throw new Error(message)
  }

  return data
}

export const api = {
  getBooks: () => request('/api/books'),
  getBook: (id) => request(`/api/books/${id}`),
  addBook: (book) => request('/api/books', 'POST', book),
  updateBook: (id, book) => request(`/api/books/${id}`, 'PUT', book),
  deleteBook: (id) => request(`/api/books/${id}`, 'DELETE'),

  getUsers: () => request('/api/users'),
  getUser: (id) => request(`/api/users/${id}`),
  addUser: (user) => request('/api/users', 'POST', user),
  updateUser: (id, user) => request(`/api/users/${id}`, 'PUT', user),
  deleteUser: (id) => request(`/api/users/${id}`, 'DELETE'),

  getIssues: () => request('/api/issued'),
  getIssuesByUser: (userId) => request(`/api/issued/user/${userId}`),
  issueBook: (req) => request('/api/issued/issue', 'POST', req),
  returnBook: (issuedId) => request(`/api/issued/return/${issuedId}`, 'PUT'),

  getFines: () => request('/api/fines')
}
