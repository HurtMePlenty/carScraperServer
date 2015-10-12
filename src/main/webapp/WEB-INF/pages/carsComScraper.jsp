<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<html>
<body>
<h1>Cars.com scraper</h1>

<div>
    <c:choose>
        <c:when test="${isRunning}">

            <p>
                Service is running
            </p>

            <p>
                Currently loaded ${itemsLoaded}
            </p>

            <form action="./carsComScraper/stop" method="post">
                <input type="submit" value="stop">
            </form>

        </c:when>
        <c:otherwise>

            <p>
                Service is not working
            </p>

            <form action="./carsComScraper/run" method="post">
                <input type="submit" value="run">
            </form>

        </c:otherwise>
    </c:choose>

    <p>
        Cars total in DB: ${itemsInDB}
    </p>

</div>
</body>
</html>