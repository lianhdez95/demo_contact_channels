import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class GalleryPickChannelPage extends StatefulWidget {
  const GalleryPickChannelPage({Key? key}) : super(key: key);

  @override
  State<GalleryPickChannelPage> createState() => _GalleryPickChannelPageState();
}

class _GalleryPickChannelPageState extends State<GalleryPickChannelPage> {
  String _imagePath = '';
  static const platform = MethodChannel('com.example.gallery.pick');

  Future<void> _openGallery() async {
    try {
      final String? result = await platform.invokeMethod('openGallery');
      if (result != null) {
        setState(() => _imagePath = result);
      }
    } on PlatformException catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(e.message ?? 'Ocurrió un error desconocido')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Gallery Example')),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              (_imagePath.isNotEmpty)
                  ? Image.file(File.fromUri(Uri.parse(
                      _imagePath))) // Use Uri.parse to convert the path to a Uri
                  : Center(child: Column(
                   children: [
                      SizedBox(height: MediaQuery.of(context).size.height*0.02),
                      const Icon(Icons.image_not_supported, size: 100, color: Colors.grey,),
                      SizedBox(height: MediaQuery.of(context).size.height*0.02),
                      const Text('No image displayed'),
                    ],
                  )),
            ],
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _openGallery,
        child: const Icon(Icons.image),
      ),
    );
  }
}
