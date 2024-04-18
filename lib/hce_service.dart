import 'package:flutter/services.dart';

const MethodChannel _channel = MethodChannel('com.example.my_flutter_app/nfc');

class NfcService {
  static Future<String> startNfc() async {
    try {
      return await _channel.invokeMethod('startNfc');
    } on PlatformException catch (e) {
      print("Failed to start NFC: '${e.message}'.");
      return "null";
    }
  }
}
