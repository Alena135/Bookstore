async function deleteBook(bookId) {
    if (!confirm("Are you sure you want to delete this book?")) return;

    try {
        const response = await fetch(`/books/${bookId}`, {
            method: "DELETE",
        });

        if (response.ok) {
            alert("Book deleted successfully!");
            // Redirect or reload the page to update the book list
            window.location.href = "/books-web/view-all";
        } else {
            alert("Failed to delete the book.");
        }
    } catch (error) {
        console.error("Error deleting book:", error);
        alert("An error occurred while deleting the book.");
    }
}
