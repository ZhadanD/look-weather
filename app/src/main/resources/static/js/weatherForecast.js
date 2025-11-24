let days = [];

async function selectCity() {
    let city = document.getElementById("select-city").value;

    days = await getDays(city);

    showSelectDate(days[0].date);

    showDayByIndex(0);
}

async function getDays(city) {
    let response = await fetch(`/weather?city=${city}`);

    switch (response.status) {
        case 200:
            let days = (await response.json()).data;

            return days;
    }
}

function showSelectDate(date) {
    let select = `
        <div class="select-container">
            <label class="select-label">Выберите дату</label>
            <select id="select-date" onchange="showDayByIndex()" class="custom-select">
            <option value="0" selected>${date}</option>
    `;

    for (let i = 1; i < days.length; i++) {
        select += `
            <option value="${i}">${days[i].date}</option>
        `;
    }

    select += `
        </select>
        </div>
    `;

    document.getElementById('dates').innerHTML = select
}

async function showDayByIndex(index) {
    let myChart = document.getElementById("myChart");

    let existingChart = Chart.getChart(myChart);

    if (existingChart) existingChart.destroy();

    let ctxMyChart = myChart.getContext("2d");

    ctxMyChart.clearRect(0, 0, myChart.width, myChart.height);

    if(index == undefined)
        index = document.getElementById('select-date').value

    let day = days[index];

    let hours = day.weatherForecast.map((forecast) => forecast.hour);

    let temperature = day.weatherForecast.map(
        (forecast) => forecast.temperature
    );

    const data = {
        labels: hours,
        datasets: [
            {
                label: "Температура",
                data: temperature,
                backgroundColor: "rgba(54, 162, 235, 0.2)",
                borderColor: "rgba(54, 162, 235, 1)",
                borderWidth: 2,
            },
        ],
    };

    const config = {
        type: "line",
        data: data,
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: `Прогноз погоды на: ${day.date}`,
                },
            },
        },
    };

    const ctx = document.getElementById("myChart").getContext("2d");
    new Chart(ctx, config);
}
