async function searchBook() {
    try {
        // Get values from input fields
        const searchBy = document.getElementById("searchBy").value;
        const query = document.getElementById("query").value;

        // Log the selected values for debugging
        console.log("Search parameters:", { searchBy, query });

        // Build the URL with required parameters
        let url = `/books/search?searchBy=${encodeURIComponent(searchBy)}`;

        // Add the query parameter if it has a value
        if (query && query.trim() !== '') {
            url += `&query=${encodeURIComponent(query)}`;
        }

        // Add default values for the other required parameters
        url += '&page=0&size=10&sortBy=title&direction=ASC';

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
                <td>${book.id || ''}</td>
                <td>${book.title || ''}</td>
                <td>${book.author || ''}</td>
                <td>${book.genre || ''}</td>
                <td>
                    <button onclick="deleteBook(${book.id})">Delete</button>
                </td>
            `;
            bookList.appendChild(bookRow);
        });
    } else {
        bookList.innerHTML = '<tr><td colspan="5">Invalid response format</td></tr>';
    }
}