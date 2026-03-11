package com.example.hrbank.global.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {

  public static String getUserIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }
}