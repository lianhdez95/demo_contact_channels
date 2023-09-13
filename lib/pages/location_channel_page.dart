import 'package:another_contact_channel/widgets/custom_card.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class LocationChannelPage extends StatefulWidget {
  const LocationChannelPage({super.key});

  @override
  State<LocationChannelPage> createState() => _LocationChannelPageState();
}

class _LocationChannelPageState extends State<LocationChannelPage> {

  final MethodChannel locationChannel = const MethodChannel(
      'com.example.location');

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
      final Map<dynamic, dynamic> result = await locationChannel.invokeMethod(
          'getLocationInfo');
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
            FutureBuilder<Map<String, dynamic>>(
              future: _getLocationInfoFromPlatform(),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        showLocationInfo('Waiting', 'Waiting'),
                        SizedBox(height: MediaQuery.of(context).size.height*0.05),
                        const CircularProgressIndicator()
                      ],
                    ),
                  );
                } else if (snapshot.hasError) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        showLocationInfo('Error getting info', 'Error getting info'),
                      ],
                    ),
                  );
                } else {
                  final latitude = snapshot.data?['latitude'];
                  final longitude = snapshot.data?['longitude'];
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        showLocationInfo(latitude, longitude),
                      ],
                    ),
                  );
                }
              },
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: (){
          _getLocationInfo(context);
        },
        child: const Icon(Icons.not_listed_location_outlined)
      ),
    );
  }

  Widget showLocationInfo(dynamic latitude, dynamic longitude) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        const Icon(Icons.location_on, size: 80, color: Colors.blue,),
        SizedBox(height: MediaQuery
            .of(context)
            .size
            .height * 0.05),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('Latitude'),
            customCard(latitude.toString())
          ],
        ),

        SizedBox(height: MediaQuery
            .of(context)
            .size
            .height * 0.05),

        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('Longitude'),
            customCard(longitude.toString())
          ],
        ),

      ],
    );
  }
}
