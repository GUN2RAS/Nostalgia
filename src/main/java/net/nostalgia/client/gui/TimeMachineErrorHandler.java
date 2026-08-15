package net.nostalgia.client.gui;

public class TimeMachineErrorHandler {
  public TimeMachineErrorHandler() {
  }

  public static String getLampGenString(String errorCode) {
    return "ERROR";
  }

  public static String getLampVerString(String errorCode) {
    if (errorCode != null && !errorCode.trim().isEmpty()) {
      String clean = errorCode.length() > 8 ? errorCode.substring(0, 8) : errorCode;
      return String.format("%-8s", clean).toUpperCase();
    } else {
      return "UNSTABLE";
    }
  }

  public static String getDetailedErrorMessage(String errorCode) {
    if (errorCode == null) {
      return "Unstable zone: move at least 10 chunks away to start a new source.";
    } else {
      String var1 = errorCode.trim().toUpperCase();
      switch (var1) {
        case "NO SKY":
          return "Sky access obstructed. The Time Machine requires a clear view of the sky or a dimension without a ceiling.";
        case "< 64 Y":
          return "Altitude too low. The Time Machine must be placed near or above sea level (Y=64).";
        case "TOO HIGH":
          return "Altitude too high. The Sky Portal rift would exceed the maximum dimension build height.";
        case "NO SCAN":
          return "Quantum scan required. Right-click the hologram display to initiate a dimensional scan.";
        default:
          return "Unstable zone: move at least 10 chunks away to start a new source.";
      }
    }
  }
}
