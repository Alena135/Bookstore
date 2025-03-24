async function searchBook() {
    try {
        // Get values from input fields
        const searchBy = document.getElementById("searchBy").value;
        const query = document.getElementById("query").value.trim();

        // Log the selected values for debugging
        console.log("Search parameters:", { searchBy, query });

        // Fallback if query is empty (you could also handle this differently)
        const searchQuery = "all";

        // Build the URL with path parameters
        // const url = `/books/search/${encodeURIComponent(searchBy)}/${encodeURIComponent(searchQuery)}`;

        let url = query
            ? `/books/search/${encodeURIComponent(searchBy)}/${encodeURIComponent(query)}`
            : `/books/search/${encodeURIComponent(searchQuery)}/${encodeURIComponent(searchQuery)}`;

        console.log("Request URL:", url);

        // Send the request
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        console.log("Response data:", data);

        // Update UI with results
        displayResults(data);

    } catch (error) {
        console.error("Error in searchBook:", error);
        alert("An error occurred while searching for books. Please try again.");
    }
}

function displayResults(data) {
    const bookList = document.getElementById("bookList");
    if (!bookList) {
        console.error("Element 'bookList' not found");
        return;
    }

    bookList.innerHTML = '';  // Clear previous results

    if (data.content && Array.isArray(data.content)) {
        if (data.content.length === 0) {
            bookList.innerHTML = '<tr><td colspan="5">No books found</td></tr>';
            return;
        }

        data.content.forEach(book => {
            const bookRow = document.createElement('tr');
            bookRow.innerHTML = `
                <td>${book.title || ''}</td>
                <td>${book.author.name + ' ' + book.author.surname || ''}</td>
                <td>${book.price || ''}</td>
                <td>
                   <button onclick="viewBook(${book.id})">View</button>
                    <button onclick="editBook(${book.id})">Edit</button>
                    <button onclick="deleteBook(${book.id})">Delete</button>
                </td>
            `;
            bookList.appendChild(bookRow);
        });
    } else {
        bookList.innerHTML = '<tr><td colspan="5">Invalid response format</td></tr>';
    }
}
