function formatDate(dtString) {
  if (dtString == null) {
    return '...'
  }

  var parsedDate = Date.parse(dtString)
  var dt = new Date(parsedDate)

  return dt.toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'short',
    year: '2-digit',
  })
}

export default formatDate
