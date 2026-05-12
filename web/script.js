let chart = null;

document.getElementById("themeBtn").addEventListener("click", () => {
    document.body.classList.toggle("dark-mode");
});

function rank() {

    const jd = document.getElementById("jd").value.trim();
    const r1 = document.getElementById("r1").value.trim();
    const r2 = document.getElementById("r2").value.trim();
    const r3 = document.getElementById("r3").value.trim();

    const important = document.getElementById("important").value.trim();

    const removeStopwords =
        document.getElementById("removeStopwords").checked;

    const ignoreCase =
        document.getElementById("ignoreCase").checked;

    const removeNumbers =
        document.getElementById("removeNumbers").checked;

    let count = 0;

    if (r1) count++;
    if (r2) count++;
    if (r3) count++;

    document.getElementById("taskCount").innerText = count;

    const body = [
        jd,
        r1,
        r2,
        r3,
        important,
        removeStopwords,
        ignoreCase,
        removeNumbers
    ].join("###");

    fetch("http://localhost:8080/rank", {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: body
    })
    .then(res => res.json())
    .then(data => render(data))
    .catch(err => {
        alert("Error: " + err);
    });
}

function render(data) {

    const result = document.getElementById("result");

    result.innerHTML = "";

    data.resumes.forEach(r => {

        const card = document.createElement("div");

        card.className = "resume-card";

        card.innerHTML = `
            <h3>${r.name}</h3>

            <p>
                <b>Match:</b>
                ${r.scorePercent}%
            </p>

            <p>
                <b>Category:</b>
                ${r.fitCategory}
            </p>

            <p>
                <b>Explanation:</b>
                ${r.explanation}
            </p>

            <h4>Matched Skills</h4>

            ${r.matchedKeywords.map(
                k => `<span class="badge match">${k}</span>`
            ).join("")}

            <h4>Missing Skills</h4>

            ${r.missingKeywords.map(
                k => `<span class="badge miss">${k}</span>`
            ).join("")}

            <h4>AI Interview Questions</h4>

            <ul>
                ${generateQuestions(r)}
            </ul>
        `;

        result.appendChild(card);
    });

    createChart(data);
}

function generateQuestions(r) {

    let html = "";

    r.matchedKeywords.slice(0,3).forEach(skill => {
        html += `
            <li>
                Explain your experience in ${skill}
            </li>
        `;
    });

    r.missingKeywords.slice(0,2).forEach(skill => {
        html += `
            <li>
                What do you know about ${skill}?
            </li>
        `;
    });

    return html;
}

function createChart(data) {

    const old = document.getElementById("chart");

    if (old) old.remove();

    const canvas = document.createElement("canvas");

    canvas.id = "chart";

    document.getElementById("result")
        .appendChild(canvas);

    const ctx = canvas.getContext("2d");

    new Chart(ctx, {

        type: "bar",

        data: {

            labels: data.resumes.map(r => r.name),

            datasets: [{
                label: "Resume Match %",
                data: data.resumes.map(r => r.scorePercent)
            }]
        },

        options: {
            responsive: true
        }
    });
}
