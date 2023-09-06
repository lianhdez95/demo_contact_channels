import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class LocationChannelPage extends StatefulWidget {
  const LocationChannelPage({super.key});

  @override
  State<LocationChannelPage> createState() => _LocationChannelPageState();
}

class _LocationChannelPageState extends State<LocationChannelPage> {

 final MethodChannel locationChannel = const MethodChannel('com.example.location');

 Future<void> _getLocationInfo(BuildContext context) async {
   try {
     await locationChannel.invokeMethod('getLocationInfo');
   } on PlatformException catch (e) {
     ScaffoldMessenger.of(context).showSnackBar(
       SnackBar(content: Text('Error: ${e.message}')),
     );
   }
 }

 Future<Map<String, dynamic>> _getLocationInfoFromPlatform() async {
   try {
     final Map<dynamic, dynamic> result = await locationChannel.invokeMethod('getLocationInfo');
     return Map<String, dynamic>.from(result);
   } on PlatformException catch (e) {
     return Future.error(e.message as Object);
   }
 }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Location Demo'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: () => _getLocationInfo(context),
                child: const Text('Get Coordinates'),
              ),
              const SizedBox(height: 20),
              FutureBuilder<Map<String, dynamic>>(
                future: _getLocationInfoFromPlatform(),
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const CircularProgressIndicator();
                  } else if (snapshot.hasError) {
                    return Text('Error: ${snapshot.error}');
                  } else {
                    final latitude = snapshot.data?['latitude'];
                    final longitude = snapshot.data?['longitude'];
                    return Text(
                      'Ubicación actual:\nLatitud: $latitude\nLongitud: $longitude',
                      textAlign: TextAlign.center,
                    );
                  }
                },
              ),
            ],
          ),
        ),
      );
  }
}
