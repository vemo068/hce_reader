import 'package:flutter/material.dart';
import 'package:hce_reader/hce_service.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: NfcReaderScreen(),
    );
  }
}

class NfcReaderScreen extends StatefulWidget {
  const NfcReaderScreen({super.key});

  @override
  _NfcReaderScreenState createState() => _NfcReaderScreenState();
}

class _NfcReaderScreenState extends State<NfcReaderScreen> {
  String _nfcResult = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('NFC Reader'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () async {
                var result = await NfcService.startNfc();
                setState(() {
                  _nfcResult = result ?? 'Failed to read NFC';
                });
              },
              child: const Text('Start NFC Reader'),
            ),
            const SizedBox(height: 20),
            const Text(
              'NFC Result:',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),
            Text(
              _nfcResult,
              style: const TextStyle(fontSize: 16),
            ),
          ],
        ),
      ),
    );
  }
}
