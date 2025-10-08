const screens = {
  splash: document.getElementById("splash"),
  onboarding: document.getElementById("onboarding"),
  home: document.getElementById("home"),
  result: document.getElementById("result"),
};

const onboardingSlide = document.getElementById("onboarding-slide");
const progressIndicator = document.getElementById("progress-indicator");
const skipBtn = document.getElementById("skip-btn");
const backBtn = document.getElementById("back-btn");
const nextBtn = document.getElementById("next-btn");
const startBtn = document.getElementById("start-btn");
const uploadBtn = document.getElementById("upload-btn");
const cameraBtn = document.getElementById("camera-btn");
const galleryInput = document.getElementById("gallery-input");
const cameraInput = document.getElementById("camera-input");
const loadingOverlay = document.getElementById("loading");
const toast = document.getElementById("toast");

const previewImage = document.getElementById("preview-image");
const classificationValue = document.getElementById("classification-value");
const confidenceValue = document.getElementById("confidence-value");
const confidenceIndicator = document.getElementById("confidence-indicator");
const classValue = document.getElementById("class-value");
const priceValue = document.getElementById("price-value");
const timestampValue = document.getElementById("timestamp-value");
const errorMessage = document.getElementById("result-error");
const saveBtn = document.getElementById("save-btn");
const rescanBtn = document.getElementById("rescan-btn");
const resultCard = document.getElementById("result-card");

const BASE_URL_YOLO = "https://mangosoft-722758638200.asia-southeast1.run.app/";
const BASE_URL_RFR = "https://mangosoft-e87cfb72dc61.herokuapp.com/";
const API_TIMEOUT_MS = 20000;

let onboardingIndex = 0;
let selectedFile = null;
let previewUrl = null;

const slides = [
  {
    title: "Welcome to Mangosoft",
    description: "Identify your mangoes in seconds with AI-powered precision.",
    emoji: "🥭",
    accent: "",
  },
  {
    title: "Detect Mango Varieties",
    description: "Instantly recognize Carabao, Pico, and Indian (Katchamitha) mangoes.",
    emoji: "🧠",
    accent: "accent-growth",
  },
  {
    title: "Scan, Assess, and Price",
    description: "Snap or upload a photo to learn the type, quality class, and estimated price.",
    emoji: "💰",
    accent: "accent-price",
  },
];

const showScreen = (target) => {
  Object.values(screens).forEach((screen) => {
    screen.classList.remove("active");
    screen.setAttribute("aria-hidden", "true");
  });

  const activeScreen = screens[target];
  activeScreen.classList.add("active");
  activeScreen.setAttribute("aria-hidden", "false");
};

const renderOnboardingSlide = () => {
  const slide = slides[onboardingIndex];
  const accentClass = slide.accent ? ` ${slide.accent}` : "";
  onboardingSlide.innerHTML = `
    <div class="slide-visual${accentClass}" aria-hidden="true">
      <span>${slide.emoji}</span>
    </div>
    <div>
      <h2>${slide.title}</h2>
      <p>${slide.description}</p>
    </div>
  `;

  const progress = ((onboardingIndex + 1) / slides.length) * 100;
  progressIndicator.style.width = `${progress}%`;

  backBtn.style.display = onboardingIndex === 0 ? "none" : "inline-flex";
  skipBtn.style.display = onboardingIndex === 0 ? "inline-flex" : "none";
  nextBtn.style.display = onboardingIndex === slides.length - 1 ? "none" : "inline-flex";
  startBtn.style.display = onboardingIndex === slides.length - 1 ? "inline-flex" : "none";
};

const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

const showToast = (message, duration = 3000) => {
  toast.textContent = message;
  toast.hidden = false;
  setTimeout(() => {
    toast.hidden = true;
  }, duration);
};

const toggleLoading = (show) => {
  loadingOverlay.hidden = !show;
};

const resetResult = () => {
  previewImage.src = "";
  classificationValue.textContent = "—";
  confidenceValue.textContent = "—";
  confidenceIndicator.style.width = "0";
  classValue.textContent = "—";
  priceValue.textContent = "—";
  timestampValue.textContent = "—";
  errorMessage.hidden = true;
};

const determineTypeAndClass = (mangoType) => {
  if (!mangoType) {
    return {
      type: "Unknown Type",
      mangoClass: "Unknown Class",
      flags: {
        Type_Carabao: 0,
        Type_Indian: 0,
        Type_Pico: 0,
        Class_ClassA: 0,
        Class_ClassB: 0,
        Class_ClassC: 0,
        Class_ClassD: 0,
        Class_ClassE: 0,
      },
    };
  }

  const normalized = mangoType.toUpperCase();

  const typeFlags = {
    Type_Carabao: Number(/K/.test(normalized)),
    Type_Indian: Number(/I/.test(normalized)),
    Type_Pico: Number(/P/.test(normalized)),
  };

  const classFlags = {
    Class_ClassA: Number(/CLASS[\s-]*A/.test(normalized)),
    Class_ClassB: Number(/CLASS[\s-]*B/.test(normalized)),
    Class_ClassC: Number(/CLASS[\s-]*C/.test(normalized)),
    Class_ClassD: Number(/CLASS[\s-]*D/.test(normalized)),
    Class_ClassE: Number(/CLASS[\s-]*E/.test(normalized)),
  };

  const typeLabel = typeFlags.Type_Carabao
    ? "Carabao"
    : typeFlags.Type_Indian
    ? "Indian"
    : typeFlags.Type_Pico
    ? "Pico"
    : "Unknown Type";

  const classLabel = classFlags.Class_ClassA
    ? "CLASS A"
    : classFlags.Class_ClassB
    ? "CLASS B"
    : classFlags.Class_ClassC
    ? "CLASS C"
    : classFlags.Class_ClassD
    ? "CLASS D"
    : classFlags.Class_ClassE
    ? "CLASS E"
    : "Unknown Class";

  return {
    type: typeLabel,
    mangoClass: classLabel,
    flags: {
      ...typeFlags,
      ...classFlags,
    },
  };
};

const buildRfrPayload = (flags) => {
  const now = new Date();
  const year = now.getFullYear();
  const monthIndex = now.getMonth();

  const monthFields = Array.from({ length: 12 }, (_, index) => (index === monthIndex ? 1 : 0));

  const payload = {
    Year: year,
    "Type_Carabao": flags.Type_Carabao,
    "Type_Indian": flags.Type_Indian,
    "Type_Pico": flags.Type_Pico,
    "Class_Class A": flags.Class_ClassA,
    "Class_Class B": flags.Class_ClassB,
    "Class_Class C": flags.Class_ClassC,
    "Class_Class D": flags.Class_ClassD,
    "Class_Class E": flags.Class_ClassE,
    Month_January: monthFields[0],
    Month_February: monthFields[1],
    Month_March: monthFields[2],
    Month_April: monthFields[3],
    Month_May: monthFields[4],
    Month_June: monthFields[5],
    Month_July: monthFields[6],
    Month_August: monthFields[7],
    Month_September: monthFields[8],
    Month_October: monthFields[9],
    Month_November: monthFields[10],
    Month_December: monthFields[11],
  };

  return payload;
};

const fetchWithTimeout = async (resource, options = {}) => {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), options.timeout ?? API_TIMEOUT_MS);

  try {
    const response = await fetch(resource, { ...options, signal: controller.signal });
    return response;
  } finally {
    clearTimeout(timeoutId);
  }
};

const callCnnApi = async (file) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetchWithTimeout(new URL("predict", BASE_URL_YOLO).toString(), {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    throw new Error(`CNN API error: ${response.status}`);
  }

  return response.json();
};

const callRfrApi = async (payload) => {
  const response = await fetchWithTimeout(new URL("predict", BASE_URL_RFR).toString(), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error(`RFR API error: ${response.status}`);
  }

  return response.json();
};

const formatPrice = (value) => {
  if (!value || value === "Error") {
    return "Unknown";
  }

  const numeric = Number(value);
  if (Number.isFinite(numeric)) {
    return `₱${numeric.toFixed(2)}/kg`;
  }

  if (Array.isArray(value)) {
    return value.join(", ");
  }

  return value;
};

const updateResultView = ({
  mangoType,
  confidence,
  mangoClass,
  price,
}) => {
  classificationValue.textContent = mangoType ? mangoType : "Unknown";

  if (typeof confidence === "number" && Number.isFinite(confidence)) {
    const percent = Math.min(100, Math.max(0, confidence * 100));
    confidenceIndicator.style.width = `${percent}%`;
    confidenceValue.textContent = `${percent.toFixed(2)}%`;
  } else {
    confidenceIndicator.style.width = "0";
    confidenceValue.textContent = "—";
  }

  classValue.textContent = mangoClass;
  priceValue.textContent = formatPrice(price);
  timestampValue.textContent = new Date().toLocaleString();
};

const handleFileSelection = async (file) => {
  if (!file) {
    return;
  }

  resetResult();

  if (previewUrl) {
    URL.revokeObjectURL(previewUrl);
  }

  previewUrl = URL.createObjectURL(file);
  previewImage.src = previewUrl;

  toggleLoading(true);
  try {
    const cnnResponse = await callCnnApi(file);
    const { type, mangoClass, flags } = determineTypeAndClass(cnnResponse?.mango_type);

    let priceResponse = null;
    try {
      const rfrPayload = buildRfrPayload(flags);
      priceResponse = await callRfrApi(rfrPayload);
    } catch (error) {
      console.error(error);
    }

    updateResultView({
      mangoType: type,
      confidence: cnnResponse?.confidence,
      mangoClass,
      price: priceResponse,
    });

    errorMessage.hidden = true;
    showScreen("result");
  } catch (error) {
    console.error(error);
    errorMessage.hidden = false;
    updateResultView({ mangoType: "Unknown", confidence: null, mangoClass: "Unknown", price: "Unknown" });
    const timedOut = error?.name === "AbortError";
    showToast(timedOut ? "The analysis took too long. Please try again." : "We couldn't analyze the image. Please try again.");
    showScreen("result");
  } finally {
    toggleLoading(false);
  }
};

const saveResultCard = async () => {
  if (typeof html2canvas !== "function") {
    showToast("Download is unavailable offline.");
    return;
  }

  try {
    const canvas = await html2canvas(resultCard);
    canvas.toBlob((blob) => {
      if (!blob) {
        showToast("Unable to save the result right now.");
        return;
      }
      const link = document.createElement("a");
      const dateSuffix = new Date().toISOString().replace(/[:.]/g, "-");
      link.href = URL.createObjectURL(blob);
      link.download = `mangosoft-result-${dateSuffix}.png`;
      link.click();
      URL.revokeObjectURL(link.href);
      showToast("Result saved to your device!");
    });
  } catch (error) {
    console.error(error);
    showToast("Failed to save the result. Please try again.");
  }
};

skipBtn.addEventListener("click", () => {
  showScreen("home");
});

backBtn.addEventListener("click", () => {
  onboardingIndex = Math.max(0, onboardingIndex - 1);
  renderOnboardingSlide();
});

nextBtn.addEventListener("click", () => {
  onboardingIndex = Math.min(slides.length - 1, onboardingIndex + 1);
  renderOnboardingSlide();
});

startBtn.addEventListener("click", () => {
  showScreen("home");
});

uploadBtn.addEventListener("click", () => galleryInput.click());

cameraBtn.addEventListener("click", () => cameraInput.click());

galleryInput.addEventListener("change", (event) => {
  const [file] = event.target.files || [];
  if (file) {
    selectedFile = file;
    handleFileSelection(selectedFile);
  }
});

cameraInput.addEventListener("change", (event) => {
  const [file] = event.target.files || [];
  if (file) {
    selectedFile = file;
    handleFileSelection(selectedFile);
  }
});

rescanBtn.addEventListener("click", () => {
  resetResult();
  showScreen("home");
});

saveBtn.addEventListener("click", saveResultCard);

document.addEventListener("DOMContentLoaded", async () => {
  showScreen("splash");
  await delay(1800);
  showScreen("onboarding");
  renderOnboardingSlide();
});

if ("serviceWorker" in navigator) {
  window.addEventListener("load", () => {
    navigator.serviceWorker
      .register("./service-worker.js")
      .catch((error) => console.error("Service worker registration failed", error));
  });
}
